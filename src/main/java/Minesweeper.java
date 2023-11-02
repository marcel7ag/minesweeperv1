import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper {

    private class MineTile extends JButton{
        int a, b;
        boolean isMine, isFlagged;

        public MineTile(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public boolean isMine() {
            return this.isMine;
        }

        public boolean isFlagged() {
            return this.isFlagged;
        }
        public void setFlagged(boolean flagged) {
            this.isFlagged = flagged;
        }


        public int getMinesAround() {
            int minesCount = 0;

            // Check the surrounding tiles
            for(int i = this.a - 1; i <= this.a + 1; i++) {
                for(int j = this.b - 1; j <= this.b + 1; j++) {
                    // Ignore the center tile and out of bounds
                    if((i != this.a || j != this.b) && i >= 0 && i < x && j >= 0 && j < y) {
                        MineTile surroundingTile = board[i][j];
                        if (mineList.contains(surroundingTile)){
                            minesCount += 1;
                        }
                    }
                }
            }

            return minesCount;
        }
    }

    int tileSize = 70;
    int x = 8;
    int y = 8;
    int gameWidth = x * tileSize;
    int gameHeight = y * tileSize;
    boolean[][] visited = new boolean[x][y];

    JFrame gameFrame = new JFrame("Escape the Java-Matrix");

    JMenuBar menubar = new JMenuBar();
    JMenu options = new JMenu("Options");
    JMenuItem easy = new JMenuItem("Easy");
    JMenuItem intermediate = new JMenuItem("Intermediate");
    JMenuItem expert = new JMenuItem("Expert");
    JMenuItem custom = new JMenuItem("Custom");

    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JLabel mineLabel = new JLabel();
    JPanel gamePanel = new JPanel();

    int mines = 20;
    MineTile[][] board = new MineTile[x][y];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    int tilesMarked = mines;
    boolean gameOver = false;
    private Timer timer;

    public Minesweeper(){
        //gameFrame.setVisible(true);
        gameFrame.setSize(gameWidth, gameHeight);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setResizable(false);
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setLayout(new BorderLayout());

        gameFrame.setJMenuBar(menubar);
        menubar.add(options);
        options.add(easy);
        options.add(intermediate);
        options.add(expert);
        options.add(custom);

        textLabel.setFont(new Font("Abadi", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.LEFT);
        textLabel.setText("Time: 0");
        textLabel.setOpaque(true);

        mineLabel.setFont(new Font("Abadi", Font.BOLD, 25));
        mineLabel.setHorizontalAlignment(JLabel.RIGHT);
        mineLabel.setText("Agents left: " + Integer.toString(tilesMarked));
        mineLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.WEST);
        textPanel.add(mineLabel, BorderLayout.EAST);
        //textPanel.setBackground(Color.green);
        gameFrame.add(textPanel, BorderLayout.NORTH);

        gamePanel.setLayout(new GridLayout(x, y));
        //gamePanel.setBackground(Color.blue);
        gameFrame.add(gamePanel);

        for (int a = 0; a < x; a++){
            for (int b = 0; b < y; b++){
                MineTile tile = new MineTile(a, b);
                board[a][b] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Abadi", Font.PLAIN, 45));

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver){
                            return;
                        }
                        MineTile tile = (MineTile) e.getSource();

                        //leftclick
                        if (e.getButton() == MouseEvent.BUTTON1){
                            if (tile.getText() == ""){
                                if (!mineList.contains(tile)) {
                                    checkMine(tile.a, tile.b);
                                } else {
                                    revealMines();
                                }
                            }
                            else if (tile.getText().equals("1")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("2")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("3")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("4")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("5")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("6")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                            else if (tile.getText().equals("7")){
                                revealSurroundingTiles(tile.a, tile.b);
                            }
                        }
                        else if (e.getButton() == MouseEvent.BUTTON3){
                            if (tile.getText() == "" && tile.isEnabled()){
                                //tile.setText("\uD83D\uDEA9"); //bomb
                                tile.setText("\uD83D\uDD74"); //agent
                                tilesMarked -= 1;
                                mineLabel.setText("Agents left: " + Integer.toString(tilesMarked));
                            }
                            //else if (tile.getText() == "\uD83D\uDEA9"){ //bomb
                            else if (tile.getText() == "\uD83D\uDD74"){ //agent
                                tile.setText("");
                                tilesMarked += 1;
                                mineLabel.setText("Agents left: " + Integer.toString(tilesMarked));
                            }

                        }
                    }
                });
                gamePanel.add(tile);

            }
        }

        gameFrame.setVisible(true);
        setMines();
        timer = new Timer(1000, new ActionListener() {
            int secondsPassed = 0;
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                textLabel.setText("Time: " + String.valueOf(secondsPassed));
            }
        });

        markSafeTile();

    }

    void markSafeTile() {
            for (int i = 0; i < x; i++) {
                for (int j = 0; j < y; j++) {
                    MineTile tile = board[i][j];
                    if (!mineList.contains(tile) && tile.getText().isEmpty()) {
                        tile.setBackground(Color.lightGray);
                        return;
                    }
                }
            }
    }

    public void revealSurroundingTiles(int a, int b){
        // Check bounds
        if(a < 0 || a >= x || b < 0 || b >= y){
            return;
        }

        MineTile tile = board[a][b];

        // Only proceed if the tile has already been revealed
        if (tile.isOpaque()){
            int minesMarked = 0;
            int unrevealedTiles = 0;
            int incorrectFlags = 0;

            // Check the surrounding tiles
            for(int i = a-1; i <= a+1; i++) {
                for(int j = b-1; j <= b+1; j++) {
                    // Ignore the center tile and out of bounds
                    if((i != a || j != b) && i >= 0 && i < x && j >= 0 && j < y) {
                        MineTile surroundingTile = board[i][j];
                        if(surroundingTile.isFlagged()){ //getText().equals("☠") \uD83D\uDD74"
                            if(surroundingTile.isMine()){
                                minesMarked += 1;
                            } else {
                                incorrectFlags += 1;
                            }
                        }
                        if(surroundingTile.isOpaque()){
                            unrevealedTiles += 1;
                        }
                    }
                }
            }

            // If all mines are marked and there are unrevealed tiles, reveal them
            // Also check if there are no incorrectly marked flags
            if (incorrectFlags > 0) {
                // End the game
            } else if (minesMarked == tile.getMinesAround() && unrevealedTiles > 0) {
                // Reveal the surrounding tiles
                for(int i = a-1; i <= a+1; i++) {
                    for(int j = b-1; j <= b+1; j++) {
                        // Ignore the center tile and out of bounds
                        if((i != a || j != b) && i >= 0 && i < x && j >= 0 && j < y) {
                            MineTile surroundingTile = board[i][j];
                            if(surroundingTile.isOpaque() && !surroundingTile.getText().equals("☠")){
                                checkMine(i, j);
                            }
                        }
                    }
                }
            }
        }
    }



    void setMines(){
        mineList = new ArrayList<MineTile>();

        int mineLeft = mines;
        while (mineLeft > 0){
            int a = random.nextInt(x); // set by x, 0-7 with x=8 as default
            int b = random.nextInt(y); // set by y, 0-7

            MineTile tile = board[a][b];
            if (!mineList.contains(tile)){
                mineList.add(tile);
                mineLeft -= 1;
            }

        }
    }

    void revealMines(){
        for (int i = 0; i < mineList.size(); i++){
            MineTile tile = mineList.get(i);
            tile.setText("☠");  //💣 bomb "\uD83D\uDCA3"
        }
        gameOver = true;
        timer.stop();

    }

    public void checkMine(int a, int b){
        timer.start();

        if(a < 0 || a >= x || b < 0 || b >= y){
            return;
        }

        // Base case: check if the cell has already been checked
        if (visited[a][b]) {
            return;
        }

        visited[a][b] = true;

        MineTile tile = board[a][b];
        if (!tile.isOpaque()){
            return;
        }
        tile.setOpaque(false); // Reveal the tile
        tilesClicked += 1;

        int minesFound = 0;

        //check mines around the clicked field
        minesFound += countMine(a-1, b-1); //oben links

        //check mines around the clicked field
        minesFound += countMine(a-1, b-1); //oben links
        minesFound += countMine(a-1, b); //oben mitte
        minesFound += countMine(a-1, b+1); //oben rechts
        minesFound += countMine(a, b-1); //links
        minesFound += countMine(a, b+1); //rechts
        minesFound += countMine(a+1, b-1);//unten links
        minesFound += countMine(a+1, b);//unten mitte
        minesFound += countMine(a+1, b+1);//unten rechts


        if (minesFound > 0){
            tile.setText(Integer.toString(minesFound));
            tile.setBackground(Color.lightGray);

            //set colors
            if(tile.getText().equals("1")){tile.setForeground(Color.blue);}
            else if(tile.getText().equals("2")){tile.setForeground(new Color(0, 153, 51));}
            else if(tile.getText().equals("3")){tile.setForeground(new Color(230, 0, 0));}
            else if(tile.getText().equals("4")){tile.setForeground(new Color(7, 13, 102));}
            else if(tile.getText().equals("5")){tile.setForeground(new Color(132, 36, 7));}
            else if(tile.getText().equals("6")){tile.setForeground(new Color(32, 110, 125));}
            else if(tile.getText().equals("7")){tile.setForeground(Color.black);}
            else if(tile.getText().equals("8")){tile.setForeground(Color.gray);}
        }
        else {
            tile.setText("");
            tile.setBackground(Color.lightGray);

            //check mines
            checkMine(a-1, b-1); //oben links
            checkMine(a-1, b+1); //oben rechts
            checkMine(a-1, b); //oben mitte
            checkMine(a, b-1); //links
            checkMine(a, b+1); //rechts
            checkMine(a+1, b-1); //unten links
            checkMine(a+1, b+1); //unten rechts
            checkMine(a+1, b); //unten mitte
        }

        if (tilesClicked == x * y - mineList.size()){
            timer.stop();
            revealMines();
            gameFrame.setTitle("You escaped the Java-Matrix!");
            mineLabel.setText("Agents left: 0");

        }
    }

    int countMine(int a, int b){
        if(a < 0 || a >= x || b < 0 || b >= y){
            return 0;
        }
        if (mineList.contains(board[a][b])){
            return 1;
        }
        return 0;
    }

}
