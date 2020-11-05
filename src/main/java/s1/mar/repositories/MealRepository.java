package s1.mar.repositories;

import s1.mar.entities.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT w FROM Meal w WHERE w.user = ?#{authentication.name}")
    List<Meal> findAllByUser();
}
