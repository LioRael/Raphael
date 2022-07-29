import request from "../utils/request";

export const getRepo = (workspace: string, repoName: string) =>
  request("/repo/detail", {
    params: {
      repoName,
      workspace,
    },
  }).then((response) => response.data);

export const getRepoList = (workspace: string) =>
  request("/repo/list", {
    params: {
      workspace,
    },
  }).then((response) => response.data);

export const createRepo = (
  workspace: string,
  name: string,
  description: string,
  visible: string
) =>
  request("/repo/create", {
    params: {
      workspace,
      name,
      description,
      visible,
    },
  }).then((response) => response.data);
