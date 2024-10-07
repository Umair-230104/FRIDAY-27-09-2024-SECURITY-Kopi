package dk.lyngby.entities;

import dk.lyngby.dto.HotelDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor

public class Hotel
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String address;


    //Hvis fetch type er lazy, som er det default, så henter den ikke rooms, men kun det man spørger om at hente fx et hotel. Hvis fetch type er eager, så henter den alt også rooms.
    //Lige nu bruger jeg en jpql query til at spørger om at hente hoteller og alle deres rooms på en gang, så det er ikke nødvendigt at bruge eager fetch type.
    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;

    public Hotel(String name, String address, List<Room> rooms)
    {
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    public Hotel(Long id, String name, String address, List<Room> rooms)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.rooms = rooms;
    }

    public Hotel(HotelDTO hotelDTO)
    {
        this.id = hotelDTO.getId();
        this.name = hotelDTO.getName();
        this.address = hotelDTO.getAddress();
        this.rooms = hotelDTO.getRooms().stream().map(roomDTO -> new Room(roomDTO, this)).collect(Collectors.toList());
    }

    @Override
    public String toString()
    {
        return "Hotel{" + "id=" + id + ", name='" + name + '\'' + ", address='" + address + '\'' +
                // Exclude rooms or provide a different way to include only necessary data
                '}';
    }
}