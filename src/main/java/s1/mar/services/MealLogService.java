package s1.mar.services;

import s1.mar.entities.Meal;
import s1.mar.repositories.MealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MealLogService {

    @Autowired
    private MealRepository mealRepository;

    @PreAuthorize("#meal.user == authentication.name")
    public void saveMeal(Meal meal) {
        mealRepository.save(meal);
    }

    public List<Meal> findMeals() {
        return mealRepository.findAllByUser();
    }

    public void deleteMeals(Integer id) {
        mealRepository.deleteById(id);
    }
}
