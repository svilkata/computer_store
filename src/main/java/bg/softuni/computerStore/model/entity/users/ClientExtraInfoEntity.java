package bg.softuni.computerStore.model.entity.users;

import bg.softuni.computerStore.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A table for contact details for a specific final order /deliveries addresses, phone numbers and notes/
 * We have connection with to FinalOrderEntity and to UserEntity
 */

@Entity
@Table(name = "clients_extra_info")
public class ClientExtraInfoEntity extends BaseEntity {
    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    @Column(columnDefinition = "TEXT")
    private String extraNotes;

    @ManyToOne
    private UserEntity user;

    public ClientExtraInfoEntity() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public ClientExtraInfoEntity setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ClientExtraInfoEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public ClientExtraInfoEntity setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
        return this;
    }

    public UserEntity getUser() {
        return user;
    }

    public ClientExtraInfoEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }
}
