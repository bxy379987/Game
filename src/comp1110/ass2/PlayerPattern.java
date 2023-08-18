package comp1110.ass2;

public class PlayerPattern extends Pattern {
    public enum ColorOption {
        c, y, r, p
    }//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色
    private ColorOption colour;
    private int dirhams; // 迪拉姆数量
    private int remainingRugs; // 剩余要放置的地毯数量

    public PlayerPattern(ColorOption colour, int dirhams, int remainingRugs) {
        this.colour = colour;
        this.dirhams = dirhams;
        this.remainingRugs = remainingRugs;
    }

    public void setColour(ColorOption colour) {
        this.colour = colour;
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

    public ColorOption getColour() {
        return colour;
    }

    @Override
    public boolean match(Pattern otherpattern) {
        PlayerPattern player=(PlayerPattern) otherpattern;
        if(this.colour==player.colour&&this.dirhams==player.dirhams&&this.remainingRugs==player.remainingRugs)
        {return true;}else return false;
    }
}
