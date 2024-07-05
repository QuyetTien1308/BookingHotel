package com.example.BookingHotel.service;

import com.example.BookingHotel.exception.InvalidBookingRequestException;
import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.repository.BookingRepository;
import com.example.BookingHotel.service.impl.IBookingService;
import com.example.BookingHotel.service.impl.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final IRoomService roomService;
    public List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRepository.findByRoomId(roomId);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    @Override
    public String saveBooking(Long roomId, BookedRoom bookingRequest) {
        if(bookingRequest.getCheckOut_Date().isBefore(bookingRequest.getCheckIn_Date())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = romIsAvailabel(bookingRequest, existingBookings);
        if(roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else {
            throw new InvalidBookingRequestException("Sorry! This room is not availabel for the selected date");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    private boolean romIsAvailabel(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckIn_Date().equals(existingBooking.getCheckIn_Date())
                                || bookingRequest.getCheckOut_Date().isBefore(existingBooking.getCheckOut_Date())
                                || (bookingRequest.getCheckIn_Date().isAfter(existingBooking.getCheckIn_Date())
                                && bookingRequest.getCheckIn_Date().isBefore(existingBooking.getCheckOut_Date()))
                                || (bookingRequest.getCheckIn_Date().isBefore(existingBooking.getCheckIn_Date())

                                && bookingRequest.getCheckOut_Date().equals(existingBooking.getCheckOut_Date()))
                                || (bookingRequest.getCheckIn_Date().isBefore(existingBooking.getCheckIn_Date())

                                && bookingRequest.getCheckOut_Date().isAfter(existingBooking.getCheckOut_Date()))

                                || (bookingRequest.getCheckIn_Date().equals(existingBooking.getCheckOut_Date())
                                && bookingRequest.getCheckOut_Date().equals(existingBooking.getCheckIn_Date()))

                                || (bookingRequest.getCheckIn_Date().equals(existingBooking.getCheckOut_Date())
                                && bookingRequest.getCheckOut_Date().equals(bookingRequest.getCheckIn_Date()))
                );
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code: " + confirmationCode));

    }

    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }
}
