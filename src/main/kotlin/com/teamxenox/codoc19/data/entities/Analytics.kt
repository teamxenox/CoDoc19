package com.teamxenox.codoc19.data.entities

import javax.persistence.*

/**
CREATE TABLE `analytics`(
id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
user_id INT NOT NULL,
feature ENUM('TEST','QUIZ','STATS','STATS_CHART') NOT NULL,
data TEXT,
created_at TIMESTAMP NOT NULL DEFAULT  CURRENT_TIMESTAMP,
FOREIGN KEY (`user_id`) REFERENCES users(`id`) ON DELETE CASCADE ON UPDATE CASCADE
);
 */
@Entity(name = "analytics")
class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var userId: Int? = null

    @Enumerated(EnumType.STRING)
    lateinit var feature: Feature

    var data: String? = null

    enum class Feature {
        QA, TEST, QUIZ, STATS, STATS_CHART
    }
}