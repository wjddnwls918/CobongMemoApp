package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo

data class Meta(
    val total_cnt: Int,
    val pageable_count: Int,
    val is_end: Boolean,
    val same_name: SameName
) {
    data class SameName(val region: List<String>, val keyword: String, val selected_region: String)

}