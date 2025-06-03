package com.labpro;

public class Kurir {
    private Integer id;
    private String name;
    private String status;

    Kurir(Integer ID, String name, String status) {
        this.id = ID;
        this.name = name;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Kurir) {
            Kurir other = (Kurir) obj;
            return this.id.equals(other.id);
        }

        return false;
    }

    @Override
    public String toString() {
        return "Kurir " + name + " dengan ID " + id + " status: " + status;
    }
}
