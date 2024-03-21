import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WarcabyGUI extends JPanel {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 60;
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_BLACK = Color.BLACK;
    private static final Color COLOR_PLAYER1 = Color.RED;
    private static final Color COLOR_PLAYER2 = Color.BLUE;
    private static final Color COLOR_HIGHLIGHT = Color.YELLOW;

    private int[][] board;
    private boolean isPlayer1Turn;
    private boolean isDragging;
    private int draggedRow, draggedCol;
    private int mouseX, mouseY;
    private boolean pieceJumped;

    public WarcabyGUI() {
        initializeGame();
        isPlayer1Turn = true;
        isDragging = false;

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                int row = mouseY / SQUARE_SIZE;
                int col = mouseX / SQUARE_SIZE;

                if ((isPlayer1Turn && board[row][col] == 1) || (!isPlayer1Turn && board[row][col] == 2)) {
                    isDragging = true;
                    draggedRow = row;
                    draggedCol = col;
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    int row = mouseY / SQUARE_SIZE;
                    int col = mouseX / SQUARE_SIZE;
                    if (isValidMove(draggedRow, draggedCol, row, col)) {
                        board[row][col] = board[draggedRow][draggedCol];
                        board[draggedRow][draggedCol] = 0;
                        isPlayer1Turn = !isPlayer1Turn;
                    }
                    isDragging = false;
                    pieceJumped = false; // Resetowanie po zakończeniu ruchu
                    repaint();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });
    }

    private void initializeGame() {
        board = new int[][] {
                {0, 1, 0, 1, 0, 1, 0, 1},
                {1, 0, 1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 1, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {2, 0, 2, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 0, 2, 0, 2},
                {2, 0, 2, 0, 2, 0, 2, 0}
        };
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Sprawdzenie czy pole docelowe mieści się na planszy
        if (toRow < 0 || toRow >= BOARD_SIZE || toCol < 0 || toCol >= BOARD_SIZE) {
            return false;
        }

        // Sprawdzenie czy pole docelowe jest puste
        if (board[toRow][toCol] != 0) {
            return false;
        }

        // Sprawdzenie czy pole źródłowe zawiera pionka gracza
        int playerPiece = isPlayer1Turn ? 1 : 2;
        if (board[fromRow][fromCol] != playerPiece) {
            return false;
        }

        // Sprawdzenie czy ruch to bicie pionka
        if (Math.abs(toRow - fromRow) == 2 && Math.abs(toCol - fromCol) == 2) {
            int opponentRow = (toRow + fromRow) / 2;
            int opponentCol = (toCol + fromCol) / 2;
            if (board[opponentRow][opponentCol] == (isPlayer1Turn ? 2 : 1)) {
                // Usunięcie zbitego pionka
                board[opponentRow][opponentCol] = 0;

                // Aktualizacja pola źródłowego pionka po udanym biciu
                board[toRow][toCol] = board[fromRow][fromCol];
                board[fromRow][fromCol] = 0;

                // Sprawdzenie czy istnieją kolejne możliwe bicie
                boolean hasMoreJumps = false;
                for (int i = -2; i <= 2; i += 4) {
                    for (int j = -2; j <= 2; j += 4) {
                        if (isValidMove(toRow, toCol, toRow + i, toCol + j)) {
                            hasMoreJumps = true;
                            break; // Przerwanie pętli, jeśli istnieje kolejne bicie
                        }
                    }
                }
                pieceJumped = hasMoreJumps;
                return true;
            }
            return false;
        }

        // Sprawdzenie czy gracz wykonuje dodatkowy ruch po biciu
        if (pieceJumped) {
            return false;
        }

        // Sprawdzenie czy ruch jest o jedno pole na ukos
        if (Math.abs(toRow - fromRow) != 1 || Math.abs(toCol - fromCol) != 1) {
            return false;
        }

        // Sprawdzenie czy ruch odbywa się w odpowiednim kierunku dla danego gracza
        if ((isPlayer1Turn && toRow <= fromRow) || (!isPlayer1Turn && toRow >= fromRow)) {
            return false;
        }

        // Jeśli wszystkie warunki są spełnione, ruch jest prawidłowy
        return true;
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color color = ((row + col) % 2 == 0) ? COLOR_WHITE : COLOR_BLACK;
                g.setColor(color);
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                if (board[row][col] == 1) {
                    g.setColor(COLOR_PLAYER1);
                    g.fillOval(col * SQUARE_SIZE + 5, row * SQUARE_SIZE + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10);
                } else if (board[row][col] == 2) {
                    g.setColor(COLOR_PLAYER2);
                    g.fillOval(col * SQUARE_SIZE + 5, row * SQUARE_SIZE + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10);
                }
                if (isDragging && draggedRow == row && draggedCol == col) {
                    g.setColor(COLOR_HIGHLIGHT);
                    g.fillOval(col * SQUARE_SIZE + 5, row * SQUARE_SIZE + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10);
                }
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(BOARD_SIZE * SQUARE_SIZE, BOARD_SIZE * SQUARE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Gra w Warcaby");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                WarcabyGUI gamePanel = new WarcabyGUI();
                frame.add(gamePanel);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}

