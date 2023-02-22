package cn.jaylong.nias.feature.foryou

import cn.jaylong.nias.core.domain.model.FollowableTopic

/**
 * @author jaylong
 * @url https://jaylong.cn
 * @date 2023/2/13
 */
sealed interface OnboardingUiState {

    object Loading : OnboardingUiState

    object LoadFailed : OnboardingUiState

    object NotShown : OnboardingUiState

    data class Shown(
        val topics: List<FollowableTopic>,
    ) : OnboardingUiState {
        val isDismissable: Boolean get() = topics.any { it.isFollowed }
    }
}