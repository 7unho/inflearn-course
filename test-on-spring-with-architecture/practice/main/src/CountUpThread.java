package main.src;

public class CountUpThread extends Thread {
    int goal;
    int currentCount;

    public CountUpThread(int goal) {
        this.goal = goal;
    }

    public int getGoal() {
        return goal;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    @Override
    public void run() {
        for (currentCount = 1; currentCount < goal; currentCount++) {
            System.out.println(currentCount);
        }
    }
}
