package dk.lyngby.entities;

import dk.lyngby.dto.RoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Room
{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private Hotel hotel;
    private int number;
    private double price;

    public Room(Hotel hotel, int number, double price)
    {
        this.hotel = hotel;
        this.number = number;
        this.price = price;
    }

    public Room(RoomDTO roomDTO, Hotel hotel)
    {
        this.id = roomDTO.getId();
        this.hotel = hotel;
        this.number = roomDTO.getNumber();
        this.price = roomDTO.getPrice();
    }
}