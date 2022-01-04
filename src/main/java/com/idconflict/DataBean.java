import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBean implements Serializable {
    private Boolean isPre;
    private List<String> preStringList;

    public Boolean getPre() {
        return isPre;
    }

    public void setPre(Boolean pre) {
        isPre = pre;
    }

    public List<String> getPreStringList() {
        return preStringList;
    }

    public void setPreStringList(List<String> preStringList) {
        this.preStringList = preStringList;
    }

    public DataBean() {

    }

    public DataBean(Boolean isPre, List<String> preStringList) {
        this.isPre = isPre;
        this.preStringList = preStringList;
    }
}
