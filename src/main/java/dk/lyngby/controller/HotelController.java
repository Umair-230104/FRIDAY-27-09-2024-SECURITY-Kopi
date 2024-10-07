package dk.lyngby.controller;

import dk.lyngby.dao.HotelDAO;
import dk.lyngby.dto.HotelDTO;
import dk.lyngby.dto.RoomDTO;
import dk.lyngby.entities.Hotel;
import dk.lyngby.exception.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HotelController
{
    private final Logger log = LoggerFactory.getLogger(HotelController.class);
    HotelDAO hotelDAO;

    public HotelController(HotelDAO hotelDAO)
    {
        this.hotelDAO = hotelDAO;
    }

    public void getHotelById(Context ctx)
    {
        try
        {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));

            // == querying ==
            Hotel hotel = hotelDAO.getById(id);

            // == response ==
            HotelDTO hotelDTO = new HotelDTO(hotel);
            ctx.res().setStatus(200);
            ctx.json(hotelDTO, HotelDTO.class);
        } catch (NumberFormatException e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getAllHotels(Context ctx)
    {
        try
        {
            // == querying ==
            List<Hotel> hotels = hotelDAO.getAll();

            // == response ==
            List<HotelDTO> hotelDTOs = HotelDTO.toHotelDTOList(hotels);
            ctx.res().setStatus(200);
            ctx.json(hotelDTOs, HotelDTO.class);
        } catch (Exception e)
        {
            log.error("500 {} ", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    public void createHotel(Context ctx)
    {
        try
        {
            // == request ==
            HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);

            // == querying ==
            Hotel hotel = new Hotel(hotelDTO);
            hotelDAO.createHotel(hotel);

            // == response ==
            ctx.res().setStatus(201);
            ctx.result("Hotel created");
        } catch (Exception e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    // After
    public void updateHotel(Context ctx)
    {
        try
        {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));
            HotelDTO hotelDTO = ctx.bodyAsClass(HotelDTO.class);

            // == querying ==
            Hotel hotel = hotelDAO.getById(id);
            hotelDAO.updateHotel(hotel, hotelDTO);

            // == response ==
            ctx.res().setStatus(200);
            ctx.result("Hotel updated");
        } catch (NumberFormatException e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        } catch (Exception e)
        {
            // Log an error if there is an error
            log.error("500 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(500, e.getMessage());
        }
    }

    public void deleteHotel(Context ctx)
    {
        // == request ==
        long id = Long.parseLong(ctx.pathParam("id"));

        // == querying ==
        Hotel hotel = hotelDAO.getById(id);

        // == response ==
        hotelDAO.delete(id);
        ctx.res().setStatus(200);
        ctx.result("Hotel deleted");
    }

    public void roomForSpecificHotel(Context ctx)
    {
        try
        {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));

            // == querying ==
//            List<Room> rooms = hotelDAO.getAllRoomsByHotel(id);
            List<RoomDTO> rooms = hotelDAO.getAllRoomsByHotel(id);

            // == response ==
            ctx.res().setStatus(200);
            ctx.json(rooms);
        } catch (NumberFormatException e)
        {
            log.error("400 {} ", e.getMessage());
            throw new ApiException(400, e.getMessage());
        }
    }
}
