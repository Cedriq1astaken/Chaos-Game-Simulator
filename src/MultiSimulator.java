import java.util.ArrayList;

public class MultiSimulator {
    private int limit;
    private double step;
    private int curr = 3;
    private int interations = 20_000;
    private ChaosGameSettings set;
    private ChaosGameSimulator sim;

    private double radius;
    private int edge;
    private String rule;
    private double width;
    private double height;

    private ArrayList<ChaosGameSimulator> simulators;

    public MultiSimulator(double radius, int edge, String rule, double width, double height, int limit, double step){
        this.limit = limit;
        this.step = step;
        this.radius = radius;
        this.edge = edge;
        this.rule = rule;
        this.width = width;
        this.height = height;
    }

    public ArrayList<ChaosGameSimulator> nextSims(){
        simulators = new ArrayList<>();
        double r = step;
        while (r < 1) {
            set = new ChaosGameSettings(curr, edge, r, radius, width, height, Helper.skipsConditionsWriter(rule));
            sim = new ChaosGameSimulator(set);

            for (int i = 0; i < interations; i++) {
                sim.nextPoint();
            }
            r += step;
            simulators.add(sim);
        }
        System.out.println("DONE");
        return simulators;
    }

}
