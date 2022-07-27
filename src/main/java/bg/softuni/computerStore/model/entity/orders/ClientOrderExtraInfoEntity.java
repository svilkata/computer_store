package bg.softuni.computerStore.model.entity.orders;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A 2nd table for extra order info such as delivery address, phone number and notes/
 * We have connection to FinalOrderEntity and to UserEntity
 */

@Entity
@Table(name = "client_orders_extra_info")
public class ClientOrderExtraInfoEntity extends BaseEntity {
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String extraNotes;

    @ManyToOne
    private UserEntity user;

    public ClientOrderExtraInfoEntity() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public ClientOrderExtraInfoEntity setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ClientOrderExtraInfoEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public ClientOrderExtraInfoEntity setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public ClientOrderExtraInfoEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
