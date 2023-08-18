package comp1110.ass2;

public class PlayerPattern extends Pattern {
    private char color;//"c"表示青色，"y"表示黄色，"r"表示红色，"p"表示紫色

    private int dirhams; // 迪拉姆数量
    private int remainingRugs; // 剩余要放置的地毯数量

    public PlayerPattern(char color, int dirhams, int remainingRugs) {
        this.color = color;
        this.dirhams = dirhams;
        this.remainingRugs = remainingRugs;
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
