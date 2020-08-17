package com.mark.service.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author Marcus lv
 * @date 2020/8/13 14:29
 */
@Table(name = "back", schema = "flight", catalog = "")
public class BackEntity {
    private long id;
    private Timestamp date;
    private Timestamp now;
    private double price;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "date")
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Basic
    @Column(name = "now")
    public Timestamp getNow() {
        return now;
    }

    public void setNow(Timestamp now) {
        this.now = now;
    }

    @Basic
    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BackEntity that = (BackEntity) o;
        return id == that.id &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(now, that.now);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, now, price);
    }
}
