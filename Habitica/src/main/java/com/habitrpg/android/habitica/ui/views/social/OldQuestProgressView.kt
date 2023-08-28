package com.habitrpg.android.habitica.ui.views.social

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.databinding.QuestCollectBinding
import com.habitrpg.android.habitica.databinding.QuestProgressOldBinding
import com.habitrpg.android.habitica.extensions.setScaledPadding
import com.habitrpg.android.habitica.models.inventory.QuestContent
import com.habitrpg.android.habitica.models.inventory.QuestProgress
import com.habitrpg.android.habitica.models.inventory.QuestProgressCollect
import com.habitrpg.android.habitica.models.user.User
import com.habitrpg.android.habitica.ui.views.HabiticaIcons
import com.habitrpg.android.habitica.ui.views.HabiticaIconsHelper
import com.habitrpg.common.habitica.extensions.getThemeColor
import com.habitrpg.common.habitica.extensions.layoutInflater
import com.habitrpg.common.habitica.extensions.loadImage

class OldQuestProgressView : LinearLayout {
    private val binding = QuestProgressOldBinding.inflate(context.layoutInflater, this)

    constructor(context: Context) : super(context) {
        setupView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setupView(context)
    }

    private fun setupView(context: Context) {
        orientation = VERTICAL

        setScaledPadding(context, 16, 16, 16, 16)

        binding.bossHealthView.setSecondaryIcon(HabiticaIconsHelper.imageOfHeartLightBg())
        binding.bossHealthView.setDescriptionIcon(HabiticaIconsHelper.imageOfDamage())
        binding.bossRageView.setSecondaryIcon(HabiticaIconsHelper.imageOfRage())
    }

    fun setData(quest: QuestContent, progress: QuestProgress?) {
        binding.collectionContainer.removeAllViews()
        if (quest.isBossQuest) {
            binding.bossNameView.text = quest.boss?.name
            if (progress != null) {
                binding.bossHealthView.set(progress.hp, quest.boss?.hp?.toDouble() ?: 0.0)
            }
            if (quest.boss?.hasRage == true) {
                binding.bossRageView.visibility = View.VISIBLE
                binding.bossRageView.set(progress?.rage ?: 0.0, quest.boss?.rage?.value ?: 0.0)
            } else {
                binding.bossRageView.visibility = View.GONE
            }
            binding.bossNameView.visibility = View.VISIBLE
            binding.bossHealthView.visibility = View.VISIBLE
            binding.collectedItemsNumberView.visibility = View.GONE
        } else {
            binding.bossNameView.visibility = View.GONE
            binding.bossHealthView.visibility = View.GONE
            binding.bossRageView.visibility = View.GONE
            binding.collectedItemsNumberView.visibility = View.VISIBLE

            if (progress != null) {
                val inflater = LayoutInflater.from(context)
                for (collect in progress.collect ?: emptyList<QuestProgressCollect>()) {
                    val contentCollect = quest.getCollectWithKey(collect.key) ?: continue
                    val collectBinding = QuestCollectBinding.inflate(inflater, binding.collectionContainer, true)
                    collectBinding.iconView.loadImage("quest_" + quest.key + "_" + collect.key)
                    collectBinding.nameView.text = contentCollect.text
                    collectBinding.valueView.set(collect.count.toDouble(), contentCollect.count.toDouble())
                }
            }
        }
    }
}
