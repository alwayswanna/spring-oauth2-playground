package a.gleb.cardservice.db.repository

import a.gleb.cardservice.db.entity.CardEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CardEntityRepository: JpaRepository<CardEntity, UUID>