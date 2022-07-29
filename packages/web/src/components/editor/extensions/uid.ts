import { Extension } from "@tiptap/core";

const UidExtension = Extension.create({
  name: "uid",
  props: {
    uid: {
      type: String,
      default: "",
    },
  },
});

export default UidExtension;
