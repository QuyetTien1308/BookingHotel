package com.example.BookingHotel.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long bookingId;
    private LocalDate checkIn_Date;
    private LocalDate checkOut_Date;
    private String guestFullName;
    private String guestEmail;
    private int NumOfAdults;
    private int NumOfChildren;
    private int totalNumOfGuest;
    private String bookingConfirmationCode;
    private RoomResponse room;

    public BookingResponse(Long bookingId, LocalDate checkIn_Date, LocalDate checkOut_Date, String bookingConfirmationCode) {
        this.bookingId = bookingId;
        this.checkIn_Date = checkIn_Date;
        this.checkOut_Date = checkOut_Date;
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
