package au.com.livewirelabs.assignment.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

@Entity(name = "stock")
public class Stock implements Serializable {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("$#,###,###.00");
    @Id
    @GeneratedValue
    private Long id;

    @Column(name="code")
    private String code;

    @Column(name="totalCost", precision = 12, scale = 2)
    private BigDecimal totalCost;

    @Column(name="totalVolume")
    private Integer totalVolume;

    public Stock() {
    }

    public Stock(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getTotalCost() {return this.totalCost; }

    public String getTotalCostStr() {
        return DECIMAL_FORMAT.format(this.totalCost);
    }

    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost.setScale(2, RoundingMode.HALF_UP); }

    public void addTotalCost(BigDecimal rate) {
        if (BigDecimal.ZERO.compareTo(rate) != 0) {
            this.totalCost = this.totalCost.add(rate, new MathContext(2)).setScale(2, RoundingMode.HALF_UP);;
        }
    }

    public int getTotalVolume() {return this.totalVolume; }

    public void setTotalVolume(Integer totalVolume) { this.totalVolume = totalVolume; }

    public void calculateTotalVolume(Integer volume) {
        this.totalVolume = Integer.sum(this.totalVolume, volume);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Stock)) {
            return false;
        }
        Stock theOtherObject = (Stock) obj;
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        equalsBuilder.append(theOtherObject.code, this.code);

        return equalsBuilder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(getCode());
        return builder.hashCode();
    }
}
