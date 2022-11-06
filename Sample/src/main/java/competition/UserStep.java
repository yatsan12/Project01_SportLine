package competition;

import android.widget.ImageView;

public class UserStep implements Comparable<UserStep>{
    private String rank,uid;
    long step;


    public UserStep(String rank, String uid, long step) {
        this.rank=rank;
        this.uid = uid;
        this.step = step;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public int compareTo(UserStep userStep) {
        return (int) (this.step - userStep.getStep());
    }

    @Override
    public String toString() {
        return "UserStep{" + "uid=" + uid + ", step=" + step +
                '}';
    }
}
