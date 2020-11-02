package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pontoon_MK3
 * Controller for DeckView.fxlm, sets images and displays in the GUI
 * Contains methods for button push action
 * @author 18025316
 * Scott Kinsmnan
 * 30/10/2020
 */

public class DeckViewController implements Initializable {
    public TextField PlayerHUD;
    public TextField DealerHUD;
    @FXML
    private ImageView deckImageView;
    @FXML
    private ImageView activeCardImageD1;
    @FXML
    private ImageView activeCardImageD2;
    @FXML
    private ImageView activeCardImageD3;
    @FXML
    private ImageView activeCardImageD4;
    @FXML
    private ImageView activeCardImageD5;
    @FXML
    private ImageView activeCardImageP1;
    @FXML
    private ImageView activeCardImageP2;
    @FXML
    private ImageView activeCardImageP3;
    @FXML
    private ImageView activeCardImageP4;
    @FXML
    private ImageView activeCardImageP5;

    private PontoonGUI game;
    private int playerCardCount = 2; // first two indexes are pre set
    private int dealerCardCount = 2;
    private ImageView[] activeCardImagesPlayer;
    private ImageView[] activeCardImagesDealer;
    private popoutWindow popOut = new popoutWindow();
    private int pTotal;
    private int dTotal;
    private String textFieldD;
    private String textFieldP;

    public void play() {
        game = new PontoonGUI();
        activeCardImagesPlayer = new ImageView[]{activeCardImageP1, activeCardImageP2, activeCardImageP3, activeCardImageP4, activeCardImageP5};
        activeCardImagesDealer = new ImageView[]{activeCardImageD1, activeCardImageD2, activeCardImageD3, activeCardImageD4, activeCardImageD5};

        activeCardImageP1.setImage(game.getP1()); // set initial hand images
        activeCardImageP2.setImage(game.getP2());
        activeCardImageD1.setImage(game.getD1());
        activeCardImageD2.setImage(game.getD2());
        activeCardImageD2.setImage(game.getBK()); // sets dealer second card image to back of card
        deckImageView.setImage(game.getBK());
        DealerHUD.appendText("Dealer: "+game.getDealerCard1());

        totalDisplayPlayer();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        play();
    }

    /**
     * When "Twist" button is pushed method called
     * Draws a card for player object and displays image in next image box
     * Add value of card to total and calls pop out window if bust (>21)
     */
    public void twistButtonPushed() {

        Image nCard = game.playerSetHand();
        activeCardImagesPlayer[playerCardCount].setImage(nCard); // iterates through image views player side
        playerCardCount++;

        if (game.busted(getTotalValuePlayer())) {
            stickButtonPushed();
            totalDisplayPlayer();
            totalDisplayDealer();
            popOut.display("Your Bust", "Better luck next time", getTotalValuePlayer(), getTotalValueDealer());
        }
        totalDisplayPlayer();
    }

    /**
     * When "Stick" button is pushed method called
     * Dealer draws cards based on player total till it
     * has a higher value than the player or over 21 ie bust
     * Calls and passes results to the popout window method
     */
    public void stickButtonPushed() {

        activeCardImageD2.setImage(game.getD2()); // flips dealer second card to reveal it

        if (getTotalValueDealer() < getTotalValuePlayer() & getTotalValueDealer() < 21 & !game.busted(getTotalValuePlayer())) {
            Image nCard = game.dealerSetHand();
            activeCardImagesDealer[dealerCardCount].setImage(nCard);
            dealerCardCount++;
            if (game.busted(getTotalValueDealer())) {
                totalDisplayDealer();
                popOut.display("Dealer Bust", "You Win Well Done!", getTotalValuePlayer(), getTotalValueDealer());
            }
        }
        if (getTotalValueDealer() >= getTotalValuePlayer() & getTotalValueDealer() <= 21) {
            totalDisplayDealer();
            totalDisplayDealer();
            popOut.display("You Loose", "Dealer Wins, Better luck next time", getTotalValuePlayer(), getTotalValueDealer());
        }
    }

    /**
     * When "play again" button is pushed method called
     * sets all images to null
     * resets player and dealer hand
     * restarts the game calling the play method
     */
    public void setPlayAgainButton() {

        for (ImageView imageView : activeCardImagesDealer) {
            imageView.setImage(null);
        }
        for (ImageView imageView : activeCardImagesPlayer) {
            imageView.setImage(null);
        }

        game.resetHand();

        playerCardCount = 2; // reset card counts
        dealerCardCount = 2;
        PlayerHUD.clear();
        DealerHUD.clear();

        play();
    }

    /**
     * @return int player total
     */
    @FXML
    protected int getTotalValuePlayer() {
        return game.getTotalValuePlayer();
    }

    /**
     * @return int dealer total
     */
    @FXML
    protected int getTotalValueDealer() {
        return game.getTotalValueDealer();
    }

    public void totalDisplayPlayer(){
        PlayerHUD.clear();
        pTotal = getTotalValuePlayer();
        textFieldP = "Player: "+pTotal+"";
        PlayerHUD.appendText(textFieldP);
    }

    public void totalDisplayDealer(){
        DealerHUD.clear();
        dTotal = getTotalValueDealer();
        textFieldD = "Dealer: "+dTotal+"";
        DealerHUD.appendText(textFieldD);
    }
}


