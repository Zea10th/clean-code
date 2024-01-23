package av.biezbardis.mentorship.tasks.third;

public class DivisionExecutor {

    private final DivisionCounter counter;
    private final DivisionOutputConstructor outputConstructor;

    public DivisionExecutor(DivisionCounter counter, DivisionOutputConstructor outputConstructor) {
        this.counter = counter;
        this.outputConstructor = outputConstructor;
    }

    String execute(int dividend, int divisor) {
        return outputConstructor.construct(counter.count(dividend, divisor));
    }
}
