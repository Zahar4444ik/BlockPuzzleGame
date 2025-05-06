import React from "react";
import Navbar from "./Navbar";
import { Outlet } from "react-router-dom";
import { Code } from "lucide-react";

const Layout = () => (
    <div className="layout">
        <Navbar />
        <main>
            <Outlet />
        </main>
        <footer className="bg-base text-gray py-4 text-center">
            <div className="flex justify-center items-center gap-2">
                <Code className="w-5 h-5" />
                <span>Built with ❤️ by Zakhar Fesiuk</span>
            </div>
        </footer>
    </div>
);

export default Layout;