package com.judge.core.interactor.usecase.rotation

import com.judge.core.domain.RotationPeriod
import com.judge.core.domain.model.Competition

class GetRotationUseCase {

    operator fun invoke(currentTime: Long, comp: Competition): Int {
        return RotationPeriod.rotationFromCurrentTime(comp.startTime, currentTime, comp.minPerBoulder)
    }

}