package Entity;

public class Riddle {
    public String question;
    public String[] choices;
    public int correctIndex;

    public Riddle(String question, String[] choices, int correctIndex) {
        this.question = question;
        this.choices = choices;
        this.correctIndex = correctIndex;
    }

    public boolean isCorrect(int choice) {
        return choice == correctIndex;
    }
}
