package bridge.bidding;

import javafx.collections.ObservableList;
import tools.Pair;

import tools.Map;
import tools.Row;

import java.util.Vector;

public class Bidding {
    // ================================================= FIELDS ====================================================
    // BiddingModule is an array of bids, dynamic array
    Vector<Bid> bidding = new Vector<Bid>();

    // Probably this will be useful
    private int dealer;
    private int turn;
    private int doubled; // 0 => neither doubled nor redoubled, 1 => doubled, 2 => redoubled.
    private int passes = 0;

    // In-pair colors proposals
    private Map<String, Integer> colorProposalsNS;
    private Map<String, Integer> colorProposalsEW;

    // ================================================= METHODS ===================================================
    public Bidding(int dealer) {
        this.dealer = dealer;
        this.turn = dealer;
        this.doubled = 0;
        colorProposalsNS = new Map<String, Integer>();
        colorProposalsEW = new Map<String, Integer>();
    }

    public int getDealer() {
        return dealer;
    }

    public Vector<Bid> getBids() {
        return bidding;
    }

    public boolean passed() {
        return passes == 4;
    }

    // We need access to the last "significant" bid and its distance from the end of the container
    private Pair<Integer, Bid> getLastBid() {
        for (int i = bidding.size() - 1; i >= 0; --i) {
            String color = bidding.get(i).getColor();
            if (!color.equals("PASS") && !color.equals("DOUBLE") && !color.equals("REDOUBLE")) {
                return new Pair<Integer, Bid>((i + dealer) % 4, bidding.get(i));
            }
        }
        return new Pair<Integer, Bid>(0, new Bid("PASS")); // This case occurs only when all players passed
    }

    // Before adding new bid to the container, we need to check whether it is correct or not, based on previous bids
    public boolean checkBidCorrectness(Bid bid) {
        // This is obvious
        if (bid.getLevel() > 7 || bid.getLevel() < 0) return false;

        // A player always can pass
        if (bid.getColor().equals("PASS")) return true;

        // A player can redouble contract only when it has been already doubled
        if (bid.getColor().equals("REDOUBLE")) {
            // A player can't redouble own contract
            if (passes % 2 == 1) return false;
            return doubled == 1;
        }

        // A player always can double enemy contract
        if (bid.getColor().equals("DOUBLE")) {
            // But not own
            if (passes % 2 == 1) return false;

            // If there is no bids
            if (getLastBid().getValue().getLevel() == 0) return false;
            return doubled == 0;
        }

        // A player can't say a bid lower than the last one
        return bid.isGreater(getLastBid().getValue());
    }

    // Add bid to the container
    public boolean addBid(Bid bid) {
        // Ask a user to give another bid, because this is incorrect
        if (!checkBidCorrectness(bid)) return false;

        // Increment passes counter, reset it in other cases
        if (bid.getColor().equals("PASS")) passes++;
        else {
            passes = 0;
            doubled = 0;
        }

        // Mark doubled/redoubled contract
        if (bid.getColor().equals("DOUBLE")) doubled = 1;
        if (bid.getColor().equals("REDOUBLE")) doubled = 2;

        // Add the bid to the container
        bidding.add(bid);

        // Update color proposals for N-S line
        if (turn % 2 == 0) {
            if (!colorProposalsNS.containsKey(bid.getColor())) colorProposalsNS.put(bid.getColor(), turn);
        }

        // Update color proposals for E-W line
        if (turn % 2 == 1) {
            if (!colorProposalsEW.containsKey(bid.getColor())) colorProposalsEW.put(bid.getColor(), turn);
        }

        // Increment turn counter
        turn = (turn + 1) % 4;
        return true;
    }

    // Check whether a pair won the bidding
    public boolean checkBidding() {
        boolean isOpened = !getLastBid().getValue().getColor().equals("PASS");
        if (!isOpened && passes == 4) return true;
        return isOpened && passes == 3;
    }

    // Get all possible bids
    public Vector<Bid> getPossibleBids() {
        String[] colors = { "CLUBS", "DIAMONDS", "HEARTS", "SPADES", "NOTRUMP" };
        Vector<Bid> correctBids = new Vector<>();
        int lastBidLevel = getLastBid().getValue().getLevel();
        if (lastBidLevel == 0) lastBidLevel = 1;

        for (int level = lastBidLevel; level < 8; level++) {
            for (int i = 0; i < 5; ++i) {
                Bid sample = new Bid(level, colors[i]);
                if (checkBidCorrectness(sample)) correctBids.add(sample);
            }
        }

        if (checkBidCorrectness(new Bid("DOUBLE"))) correctBids.add(new Bid("DOUBLE"));
        if (checkBidCorrectness(new Bid("REDOUBLE"))) correctBids.add(new Bid("REDOUBE"));
        correctBids.add(new Bid("PASS"));

        return correctBids;
    }

    // Get the winning contract
    public String getWinningContract() {
        int winner;
        Bid lastBid = getLastBid().getValue();
        int lastPlayer = getLastBid().getKey();

        if (lastPlayer % 2 == 0) {
            winner = colorProposalsNS.getValue(lastBid.getColor());
        }
        else winner = colorProposalsEW.getValue(lastBid.getColor());

        System.out.println(this.toString());

        return winner + String.valueOf(doubled) + lastBid.getLevel() + lastBid.getColor();
    }

    @Override
    public String toString() {
        String result = "";

        // An indent in the first line
        for (int i = 0; i < dealer; ++i) result += "\t";

        // Add remaining bids
        for (int i = 0; i < bidding.size(); ++i) {
            result += bidding.get(i).toString() + "\t";
            if ((dealer + i) % 4 == 3) result += "\n";
        }
        return result;
    }

    public String[][] generateHistory() {
        int rows = (int)Math.ceil((dealer + bidding.size()) / 4.0);
        String[][] result = new String[rows][4];

        for (int i = 0; i < dealer; ++i) result[0][i] = "";
        for (int i = 0; i < bidding.size(); ++i)
            result[(int)((i + dealer) / 4)][(i + dealer) % 4]
                    = bidding.get(i).toStringNoColor();
        for (int i = dealer + bidding.size(); i < 4 * rows; ++i) result[rows - 1][i % 4] = "";

        return result;
    }
}
