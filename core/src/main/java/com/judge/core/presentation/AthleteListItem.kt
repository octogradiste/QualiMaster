package com.judge.core.presentation

import com.judge.core.domain.model.Athlete
import com.judge.core.domain.model.Category
import com.judge.core.domain.model.Boulder


sealed class AthleteListItem {

    class HeaderItem(val title: String) : AthleteListItem()

    class CategoryItem(val category: Category) : AthleteListItem()

    class AthleteItem(val athlete: Athlete) : AthleteListItem()

    class BoulderItem(val athlete: Athlete, val boulder: Boulder) : AthleteListItem()

}