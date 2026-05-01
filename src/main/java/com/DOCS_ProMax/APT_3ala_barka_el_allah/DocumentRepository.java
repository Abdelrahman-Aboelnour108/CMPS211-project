package com.DOCS_ProMax.APT_3ala_barka_el_allah;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for {@link DocumentEntity}.
 *
 * Member 1 – Database Persistence & Version History
 *
 * Spring generates all implementations automatically at startup.
 * No boilerplate required.
 */
@Repository
public interface DocumentRepository extends MongoRepository<DocumentEntity, String> {

    /** Look up a document by its editor code (used by editors joining a session). */
    Optional<DocumentEntity> findByEditorCode(String editorCode);

    /** Look up a document by its viewer code (used by read-only viewers). */
    Optional<DocumentEntity> findByViewerCode(String viewerCode);

    /**
     * Searches both code fields so one call handles both editor and viewer join flows.
     * Returns the first match found or empty.
     */
    default Optional<DocumentEntity> findByEditorCodeOrViewerCode(String code) {
        Optional<DocumentEntity> byEditor = findByEditorCode(code);
        if (byEditor.isPresent()) return byEditor;
        return findByViewerCode(code);
    }

    /** Returns all documents owned by a given user (for the LIST_DOCS command). */
    List<DocumentEntity> findAllByOwnerUsername(String ownerUsername);
}
