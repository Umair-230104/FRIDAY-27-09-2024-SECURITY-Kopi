package dk.lyngby.dao;

import dk.lyngby.dto.RoomDTO;
import dk.lyngby.entities.Hotel;
import dk.lyngby.entities.Room;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RoomDAO extends DAO
{
    private final EntityManagerFactory emf;

    public RoomDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public List<Room> getAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
        }
    }

    @Override
    public Room getById(long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            return em.find(Room.class, id);
        }
    }

    @Override
    public void delete(long id)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Room room = em.find(Room.class, id);
            em.remove(room);
            em.getTransaction().commit();
        }
    }

    public void createRoom(RoomDTO roomDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, roomDTO.getHotelId());
            Room room = new Room(hotel, roomDTO.getNumber(), roomDTO.getPrice());
            em.persist(room);
            em.getTransaction().commit();
        }
    }

    public void updateRoom(Room room, RoomDTO roomDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Hotel hotel = em.find(Hotel.class, roomDTO.getHotelId());
            room.setHotel(hotel);
            room.setNumber(roomDTO.getNumber());
            room.setPrice(roomDTO.getPrice());
            em.merge(room);
            em.getTransaction().commit();
        }
    }

    @Override
    public void create(Object o)
    {
    }

    @Override
    public void update(Object o)
    {
    }
}
