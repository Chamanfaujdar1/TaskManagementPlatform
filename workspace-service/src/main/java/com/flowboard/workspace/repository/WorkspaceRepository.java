package com.flowboard.workspace.repository;

import com.flowboard.workspace.entity.Workspace;
import com.flowboard.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Integer> {

    List<Workspace> findByOwnerId(int ownerId);

    Optional<Workspace> findByWorkspaceId(int workspaceId);

    List<Workspace> findByVisibility(String visibility);

    boolean existsByNameAndOwnerId(String name, int ownerId);

    long countByOwnerId(int ownerId);

    @Query("SELECT w FROM Workspace w JOIN WorkspaceMember wm " +
            "ON w.workspaceId = wm.workspaceId WHERE wm.userId = :userId")
    List<Workspace> findByMemberUserId(@Param("userId") int userId);
}