package dk.lyngby.dto;

import dk.lyngby.entities.Hotel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor

public class HotelDTO
{
    private Long id;
    private String name;
    private String address;
    private List<RoomDTO> rooms;

    public HotelDTO(long id, String name, String address, List<RoomDTO> rooms)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    public HotelDTO(Hotel hotel)
    {
        this.id = hotel.getId();
        this.name = hotel.getName();
        this.address = hotel.getAddress();
        this.rooms = hotel.getRooms().stream().
                map(RoomDTO::new)
                .collect(Collectors.toList());
    }

    public static List<HotelDTO> toHotelDTOList(List<Hotel> hotels)
    {
        return hotels.stream().map(HotelDTO::new).collect(Collectors.toList());
    }
}