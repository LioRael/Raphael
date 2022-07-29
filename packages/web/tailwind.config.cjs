/** @type {import('tailwindcss').Config} */
const { preset } = require("twin.arco");

module.exports = {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  presets: [preset()],
  theme: {
    colors: {
      blue: "#165DFF",
    },
  },
  plugins: [],
};
