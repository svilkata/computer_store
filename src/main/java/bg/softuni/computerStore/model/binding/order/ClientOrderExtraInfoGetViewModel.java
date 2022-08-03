package bg.softuni.computerStore.model.binding.order;

import javax.validation.constraints.NotBlank;

public class ClientOrderExtraInfoGetViewModel {
    @NotBlank(message = "Delivery address can not be empty!")
    private String deliveryAddress;
    @NotBlank(message = "Phone number can not be empty!")
    private String phoneNumber;
    private String extraNotes;

    public ClientOrderExtraInfoGetViewModel() {
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public ClientOrderExtraInfoGetViewModel setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ClientOrderExtraInfoGetViewModel setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getExtraNotes() {
        return extraNotes;
    }

    public ClientOrderExtraInfoGetViewModel setExtraNotes(String extraNotes) {
        this.extraNotes = extraNotes;
        return this;
    }
}
