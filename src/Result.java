public class Result {

    private int nbCorrect;
    private int nbIncorrect;
    private double percent;
    private int total;

    public Result(int nbCorrect, int nbIncorrect) {
        this.total = nbCorrect + nbIncorrect;
        this.nbCorrect = nbCorrect;
        this.nbIncorrect = nbIncorrect;
        this.percent = (nbCorrect * 100.0 / (nbCorrect + nbIncorrect));
    }

    public Result(int total, double percent) {
        this.total = total;
        this.percent = percent;
    }

    public int getTotal() {
        return this.total;
    }

    public double getPercent() {
        return this.percent;
    }

}
