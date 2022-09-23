import java.awt.*;

public class Shape {
    private int x = 4, y = 0;
    private int normal = 600;
    private int fast = 50;
    private int delayTimeForMovement = normal;
    private long beginTime;
    private int deltaX = 0;
    private boolean collision = false;
    private int[][] coords;
    private Board board;
    private Color color;

    public Shape(int[][] coords, Board board, Color color) {
        this.coords = coords;
        this.board = board;
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void reset() {
        this.x = 4;
        this.y = 0;
        collision = false;
    }

    public int[][] getCoords() {
        return coords;
    }
    public  int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void update() {
        //Tao mau cho bang
        if (collision) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[0].length; col++) { //lay length cua column
                    if (coords[row][col] != 0) {
                        board.getBoard()[y+ row][x + col ] = color;
                    }
                    }
            }
            checkLine();
            board.addScore();
            //tao hinh dang hien tai cho shape
            board.setCurrentShape();
            return;
        }
        //Kiem tra di chuyen cua vien gach theo phuong ngang
        boolean moveX = true;
        if (!(x + deltaX + coords[0].length > Board.boardWidth) && !(x + deltaX < 0)) {
            for (int row = 0; row < coords.length; row++) {
                for (int col = 0; col < coords[row].length; col++) {
                    if(coords[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }
                    }
                }
            }
            if(moveX) {
                x += deltaX;
            }
        }
        deltaX = 0;
        if (System.currentTimeMillis() - beginTime > delayTimeForMovement) {
            // Chuyen dong theo chieu doc
            if (!(y + 1 + coords.length > Board.boardHeight)) {
                for(int row = 0; row < coords.length;row++){
                    for(int col=0; col < coords[row].length;col++){
                        if(coords[row][col] != 0){
                            if(board.getBoard()[y+1+row][x+deltaX+col] != null){
                                collision=true;
                            }
                        }
                    }
                }
                if(!collision) {
                    y++;
                }
            } else {
                collision = true;
            }

            beginTime = System.currentTimeMillis();
        }
    }
    //Ghi diem
    private void checkLine(){
        int line = board.getBoard().length -1;
        for (int i= line; i > 0; i--){
            int count =0;
            for (int col = 0;col < board.getBoard()[0].length;col++){
                if(board.getBoard()[i][col] != null){
                    count++;
                }
                board.getBoard()[line][col] = board.getBoard()[i][col];
            }
            if(count < board.getBoard()[0].length){
                line--;
            }
        }
    }
    //Xoay shape
    public void rotation(){
        int [][] rotationShape = transpose(coords);
        reverse(rotationShape);
        //Kiem tra border ben phai va ben duoi
        if(x + rotationShape[0].length > Board.boardWidth || y+ rotationShape.length > Board.boardHeight) return;
        // kiem tra va cham voi cac shape truoc khi xoay
        for(int row =0; row < rotationShape.length; row++){
            for(int col =0; col < rotationShape[row].length; col++){
                if(rotationShape[row][col] != 0){
                    if(board.getBoard()[y+row][x+col] != null){
                        return;
                    }
                }
            }
        }
        coords = rotationShape;

    }
    private int[][] transpose( int [][] matrix){
        int[][] tmp = new int[matrix[0].length][matrix.length];
        for(int row =0; row < matrix.length;row++){
            for(int col =0; col < matrix[0].length;col++){
                tmp[col][row] = matrix[row][col];
            }
        }
        return tmp;
    }
    private void reverse(int[][] matrix){
        int mid = matrix.length/2;
        for(int row =0; row < mid; row++){
            int[] tmp = matrix[row];
            matrix[row]=matrix[matrix.length - row -1];
            matrix[matrix.length - row -1]=tmp;
       }
    }

    public void render(Graphics g) {
        //Draw the shape
        for (int row = 0; row < coords.length; row++) {
            for (int col = 0; col < coords[0].length; col++) { //lay length cua column
                if (coords[row][col] != 0) {
                    g.setColor(color);
                    g.fillRect(col * Board.blockSize + x * Board.blockSize, row * Board.blockSize + y * Board.blockSize, Board.blockSize, Board.blockSize);
                }
            }
        }
    }
    //Tang toc do roi cua vien gach
    public void SpeedUp(){
        delayTimeForMovement = fast;
    }
    // Tra toc do ve trang thai binh thuong
    public void SpeedNormal(){
        delayTimeForMovement = normal;
    }
    //Di chuyen sang trai
    public void toTheLeft(){
       deltaX =-1;
    }
    //Di chuyen sang phai
    public void toTheRight(){
        deltaX =1;
    }
}
