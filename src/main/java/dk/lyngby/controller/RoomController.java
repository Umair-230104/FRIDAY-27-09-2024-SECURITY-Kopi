package dk.lyngby.controller;

import dk.lyngby.dao.RoomDAO;
import dk.lyngby.dto.RoomDTO;
import dk.lyngby.entities.Room;
import dk.lyngby.exception.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RoomController
{
    private final Logger log = LoggerFactory.getLogger(HotelController.class);
    RoomDAO roomDAO;

    public RoomController(RoomDAO roomDAO)
    {
        this.roomDAO = roomDAO;
    }

    public void getRoomById(Context ctx)
    {
        try
        {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));

            // == querying ==
            Room room = roomDAO.getById(id);

            // == response ==
            RoomDTO roomDTO = new RoomDTO(room);

            ctx.res().setStatus(200);
            ctx.json(roomDTO, RoomDTO.class);
        } catch (NumberFormatException e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void getAllRooms(Context ctx)
    {
        try
        {
            // == querying ==
            List<Room> rooms = roomDAO.getAll();

            // == response ==
            List<RoomDTO> roomDTOs = RoomDTO.toRoomDTOList(rooms);
            ctx.res().setStatus(200);
            ctx.json(roomDTOs, RoomDTO.class);
        } catch (Exception e)
        {
            log.error("500 {} ", e.getMessage());
            throw new ApiException(500, e.getMessage());
        }
    }

    public void createRoom(Context ctx)
    {
        try
        {
            // == request ==
            RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);

            // == querying ==
            roomDAO.createRoom(roomDTO);

            // == response ==
            ctx.res().setStatus(201);
            ctx.result("Room created");
        } catch (Exception e)
        {
            // Log an error if there is an error
            log.error("400 {} ", e.getMessage());

            // Throw our own exception, which we created in ApiException.java
            throw new ApiException(400, e.getMessage());
        }
    }

    public void updateRoom(Context ctx)
    {
        try
        {
            // == request ==
            long id = Long.parseLong(ctx.pathParam("id"));
            RoomDTO roomDTO = ctx.bodyAsClass(RoomDTO.class);

            // == querying ==
            Room room = roomDAO.getById(id);
            roomDAO.updateRoom(room, roomDTO);

            // == response ==
            ctx.res().setStatus(200);
            ctx.result("Room updated");
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

    public void deleteRoom(Context ctx)
    {
        // == request ==
        long id = Long.parseLong(ctx.pathParam("id"));

        // == querying ==
        Room room = roomDAO.getById(id);
        if (room == null)
        {
            ctx.res().setStatus(404);
            ctx.result("Room not found");
            return;
        }

        // == response ==
        roomDAO.delete(id);
        ctx.res().setStatus(200);
        ctx.result("room deleted");
    }
}
