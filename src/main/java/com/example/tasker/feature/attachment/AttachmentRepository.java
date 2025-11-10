package com.example.tasker.feature.attachment;

import com.example.tasker.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    List<Attachment> findByTask_Id(Long taskId);
}
