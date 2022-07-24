package bg.softuni.computerStore.model.entity.users;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * A table for all contact details /deliveries addresses and phone numbers/ of each user
 * After an order is done - a new or old contact detail is used
 */

@Entity
@Table(name = "clients_extra_info")
public class ClientExtraInfoEntity extends BaseEntity {
    private String deliveryAddress;
    private String phoneNumber;
    private String extraNotes;

    @ManyToOne
    private UserEntity user;

    @OneToOne
    private FinalOrderEntity finalOrderEntity;

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
}
