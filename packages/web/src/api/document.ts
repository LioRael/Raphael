import request from "../utils/request";

export const getDocuments = (repoId: string) =>
  request("/repo/doc/list", {
    params: {
      repoId,
    },
  }).then((response) => response.data);

export const getDocument = (repoId: string, docId: string) =>
  request("/repo/doc/detail", {
    params: {
      repoId,
      docId,
    },
  }).then((response) => response.data);

export const updateDocument = (
  repoId: string,
  docId: string,
  title: string,
  content: string
) =>
  request("/repo/doc/update", {
    method: "POST",
    data: {
      repoId,
      docId,
      title,
      content,
    },
  }).then((response) => response.data);

export const createDocument = (
  repoId: string,
  title: string,
  content: string,
  type: string,
  parentId?: string
) =>
  request("/repo/doc/create", {
    method: "POST",
    data: {
      repoId,
      title,
      type,
      parentId,
      content,
    },
  }).then((response) => response.data);

export const moveDocument = (
  repoId: string,
  docId: string,
  parentId: string,
  index: number
) =>
  request("/repo/doc/move", {
    params: {
      repoId,
      docId,
      parentId,
      index,
    },
  }).then((response) => response.data);

export const deleteDocument = (repoId: string, docId: string) =>
  request("/repo/doc/delete", {
    params: {
      repoId,
      docId,
    },
  }).then((response) => response.data);
