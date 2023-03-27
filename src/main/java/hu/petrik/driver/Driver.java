package hu.petrik.driver;

public class Driver {
    private int id;
    private String pilota;
    private int kor;
    private String nemzetiseg;

    public Driver(int id, String pilota, int kor, String nemzetiseg) {
        this.id = id;
        this.pilota = pilota;
        this.kor = kor;
        this.nemzetiseg = nemzetiseg;
    }

    public Driver(String pilota, int kor, String nemzetiseg) {
        this.pilota = pilota;
        this.kor = kor;
        this.nemzetiseg = nemzetiseg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPilota() {
        return pilota;
    }

    public void setPilota(String pilota) {
        this.pilota = pilota;
    }

    public int getKor() {
        return kor;
    }

    public void setKor(int kor) {
        this.kor = kor;
    }

    public String getNemzetiseg() {
        return nemzetiseg;
    }

    public void setNemzetiseg(String nemzetiseg) {
        this.nemzetiseg = nemzetiseg;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", pilota='" + pilota + '\'' +
                ", kor=" + kor +
                ", nemzetiseg='" + nemzetiseg + '\'' +
                '}';
    }
}
