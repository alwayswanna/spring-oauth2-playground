package a.gleb.statisticsservice.db.repository

import a.gleb.statisticsservice.db.entity.StatisticsEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface StatisticsEntityRepository : JpaRepository<StatisticsEntity, UUID>