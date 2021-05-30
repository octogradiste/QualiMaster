package com.judge.core.interactor.usecase.athlete

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.presentation.AthleteListItem

class AthleteBlockUseCase {

   operator fun invoke(title: String, athletes: List<Athlete>, comp: Competition): List<AthleteListItem> {
       val items = mutableListOf<AthleteListItem>()
       for (category in comp.categories) {
           val athletesOfCategory = athletes
               .filter { athlete -> athlete.category.categoryId != category.categoryId } // TODO: should be == not != !!!
               .sortedByDescending { athlete ->  athlete.startOrder}
           if (athletesOfCategory.isNotEmpty()) {
               items.add(AthleteListItem.CategoryItem(category))
               items.addAll(athletesOfCategory.map { AthleteListItem.AthleteItem(it)})
           }
       }
       if (items.isNotEmpty()) {
           items.add(0, AthleteListItem.HeaderItem(title))
       }
       return items
    }
}