package com.flowboard.workspace.repository;

import com.flowboard.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceMemberRepository
        extends JpaRepository<WorkspaceMember, Integer> {

    List<WorkspaceMember> findByWorkspaceId(int workspaceId);

    Optional<WorkspaceMember> findByWorkspaceIdAndUserId(
            int workspaceId, int userId);

    boolean existsByWorkspaceIdAndUserId(int workspaceId, int userId);

    void deleteByWorkspaceIdAndUserId(int workspaceId, int userId);
}