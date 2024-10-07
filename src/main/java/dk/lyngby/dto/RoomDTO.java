package dk.lyngby.dto;

import dk.lyngby.entities.Room;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor

public class RoomDTO
{
    private Long id;
    private long hotelId;
    private int number;
    private double price;

    public RoomDTO(long hotelId, int number, double price)
    {
        this.hotelId = hotelId;
        this.number = number;
        this.price = price;
    }

    public RoomDTO(long id, long hotelId, int number, double price)
    {
        this.id = id;
        this.hotelId = hotelId;
        this.number = number;
        this.price = price;
    }

    public RoomDTO(Room room)
    {
        this.id = room.getId();
        this.hotelId = room.getHotel().getId();
        this.number = room.getNumber();
        this.price = room.getPrice();
    }

    public static List<RoomDTO> toRoomDTOList(List<Room> rooms)
    {
        return rooms.stream().map(RoomDTO::new).collect(Collectors.toList());
    }
}