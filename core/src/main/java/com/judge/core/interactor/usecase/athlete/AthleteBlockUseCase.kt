package com.judge.core.interactor.usecase.athlete

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Competition
import com.judge.core.presentation.AthleteListItem

class AthleteBlockUseCase {

   operator fun invoke(title: String, athletes: List<Athlete>, comp: Competition): List<AthleteListItem> {
       val itemsBlock = mutableListOf<AthleteListItem>()
       for (category in comp.categories) {
           val itemsCategory = mutableListOf<AthleteListItem>()
           val athletesOfCategory = athletes
               .filter { athlete -> athlete.categoryName == category.name }
               .sortedByDescending { athlete ->  athlete.startOrder }
           if (athletesOfCategory.isNotEmpty()) {
               itemsCategory.add(AthleteListItem.CategoryItem(category))
               itemsCategory.addAll(athletesOfCategory.map { AthleteListItem.AthleteItem(it)})
               itemsBlock.addAll(itemsCategory)
           }
       }
       if (itemsBlock.isNotEmpty()) {
           itemsBlock.add(0, AthleteListItem.HeaderItem(title))
       }
       return itemsBlock
    }
}