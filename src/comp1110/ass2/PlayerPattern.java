package comp1110.ass2;

public class PlayerPattern extends Pattern {
    private boolean isplaying=false;
    private char color;//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色

    private int dirhams; // 迪拉姆数量
    private int remainingRugs; // 剩余要放置的地毯数量

    public PlayerPattern(String input) {
        this.color = input.charAt(1);
        String dirham=input.substring(2,3);
        this.dirhams = Integer.parseInt(dirham);
        String remainingRuy=input.substring(5,2);
        this.remainingRugs = Integer.parseInt(remainingRuy);
        if(input.charAt(7)=='i')isplaying=true;
    }

    public boolean isIsplaying() {
        return isplaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public char getColor() {
        return color;
    }

    public void setColor(char color) {
        this.color = color;
    }

    public int getDirhams() {
        return dirhams;
    }

    public void setDirhams(int dirhams) {
        this.dirhams = dirhams;
    }

    public int getRemainingRugs() {
        return remainingRugs;
    }

    public void setRemainingRugs(int remainingRugs) {
        this.remainingRugs = remainingRugs;
    }

    @Override
    public boolean match(Pattern otherpattern) {
        PlayerPattern playerPattern=(PlayerPattern) otherpattern;
        if(this.remainingRugs==playerPattern.remainingRugs&&this.dirhams==playerPattern.dirhams&&this.color==playerPattern.color)
        return true;
        else return false;
    }
}
