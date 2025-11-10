package com.example.tasker.feature.board;

import com.example.tasker.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepository  extends JpaRepository<Label,Long> {

}
