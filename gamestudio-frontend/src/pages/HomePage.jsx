import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import "../assets/css/HomePage.css";
import { useAuth } from "../context/AuthContext";
import { Star, Trophy, MessageSquare, Play } from "lucide-react";
import { getComments, addComment } from '../services/CommentService';
import { getAverageRating, setAndAddRating } from "../services/RatingService";
import { getTopScores } from "../services/ScoreService";

function HomePage() {
    const { authenticated } = useAuth();
    const [leaderboard, setLeaderboard] = useState([]);
    const [averageRating, setAverageRating] = useState(0);
    const [comments, setComments] = useState([]);
    const [rating, setRating] = useState(0);
    const [comment, setComment] = useState("");
    const [showForm, setShowForm] = useState(false);

    useEffect(() => {
        const fetchInitialData = async () => {
            // Fetch leaderboard (top 5 scores for Block Puzzle)
            const topScores = await getTopScores("Block Puzzle");
            setLeaderboard(topScores.slice(0, 5));

            // Fetch average rating for Block Puzzle (visible to all)
            const avgRating = await getAverageRating("Block Puzzle");
            setAverageRating(avgRating);

            // Fetch comments for Block Puzzle (visible to all)
            const commentList = await getComments("Block Puzzle");
            setComments(commentList.slice(0, 5));
        };

        fetchInitialData();
    }, []);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (rating < 1 || rating > 5) {
            alert("Rating must be between 1 and 5");
            return;
        }
        const nickname = localStorage.getItem("nickname");
        const ratedOn = new Date().toISOString();
        const playerDTO = { nickname };

        // Submit rating
        const ratingPayload = {
            game: "Block Puzzle",
            playerDTO,
            rating,
            ratedOn,
        };
        try {
            await setAndAddRating(ratingPayload);
        } catch (err) {
            alert("Error submitting rating: " + err.message);
            return;
        }

        // Submit comment if provided
        if (comment.trim()) {
            const commentPayload = {
                game: "Block Puzzle",
                playerDTO,
                comment,
                commentedOn: ratedOn,
            };
            try {
                await addComment(commentPayload);
            } catch (err) {
                alert("Error submitting comment: " + err.message);
                return;
            }
        }

        // Refresh rating and comments
        const updatedAvgRating = await getAverageRating("Block Puzzle");
        setAverageRating(updatedAvgRating);
        const updatedComments = await getComments("Block Puzzle");
        setComments(updatedComments.slice(0, 5));
        setShowForm(false);
        setRating(0);
        setComment("");
    };

    return (
        <div className="homepage min-h-screen flex flex-col items-center text-white">
            <div className="container max-w-4xl mx-auto text-center px-6 py-12">
                {/* Title Section */}
                <div className="mb-12">
                    <h1 className="text-5xl font-bold mb-4 text-highlight glow">
                        Block Puzzle
                    </h1>
                    <p className="text-lg text-gray">Test your skills, shine bright!</p>
                </div>

                {/* Play Button */}
                <div className="mb-16">
                    <Link to="/levels">
                        <button className="play-btn bg-section text-white py-4 px-12 rounded-lg text-xl font-semibold transition-all duration-300 transform hover:scale-105 glow-edge">
                            <span className="flex items-center gap-2">
                                <Play className="w-6 h-6 text-primary" />
                                Play Now
                            </span>
                        </button>
                    </Link>
                </div>

                {/* Leaderboard and Rating Section (Side by Side) */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-12">
                    {/* Leaderboard Section */}
                    <div className="leaderboard bg-section-bright rounded-lg p-6 fade-in">
                        <h2 className="text-2xl font-semibold mb-4 text-highlight flex items-center gap-2">
                            <Trophy className="w-6 h-6" /> Top 5 Players
                        </h2>
                        <div className="max-h-56 overflow-y-auto">
                            <ul>
                                {leaderboard.length > 0 ? (
                                    leaderboard.map((entry, index) => (
                                        <li key={entry.ident} className="border-b border-gray-700 py-2 flex justify-between text-base">
                                            <span className="text-gray">{index + 1}. {entry.player.nickname}</span>
                                            <span className="font-semibold text-primary">{entry.points} pts</span>
                                        </li>
                                    ))
                                ) : (
                                    <li className="py-2 text-gray-500">No scores yet. Be the first!</li>
                                )}
                            </ul>
                        </div>
                    </div>

                    {/* Rating Section */}
                    <div className="rating-comments bg-section-bright rounded-lg p-6 fade-in">
                        <h2 className="text-2xl font-semibold mb-4 text-highlight flex items-center gap-2">
                            <Star className="w-6 h-6" /> Rate & Share
                        </h2>
                        <div className="flex justify-between items-center mb-6">
                            <div className="flex items-center">
                                <span className="text-lg font-semibold mr-2 text-gray">Rating:</span>
                                <div className="flex">
                                    {[...Array(5)].map((_, i) => (
                                        <span key={i} className={i < averageRating ? "text-primary" : "text-gray-600"}>
                                            ⭐
                                        </span>
                                    ))}
                                    <span className="ml-2 text-gray">({averageRating})</span>
                                </div>
                            </div>
                            {authenticated && (
                                <button
                                    onClick={() => setShowForm(!showForm)}
                                    className="bg-primary hover:bg-primary-dark text-white py-2 px-6 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 glow"
                                >
                                    {showForm ? "Cancel" : "Rate & Share"}
                                </button>
                            )}
                        </div>

                        {showForm && authenticated && (
                            <form onSubmit={handleSubmit} className="mb-6 p-4 bg-gray-800 rounded-lg fade-in">
                                <div className="mb-4">
                                    <label className="block text-sm font-semibold mb-2 text-gray">Your Rating (1-5):</label>
                                    <input
                                        type="number"
                                        min="1"
                                        max="5"
                                        value={rating}
                                        onChange={(e) => setRating(Number(e.target.value))}
                                        className="w-full p-2 rounded-lg border border-gray-600 bg-gray-900 text-white focus:border-highlight transition-all duration-300"
                                        required
                                    />
                                </div>
                                <div className="mb-4">
                                    <label className="block text-sm font-semibold mb-2 text-gray">Your Comment:</label>
                                    <textarea
                                        value={comment}
                                        onChange={(e) => setComment(e.target.value)}
                                        placeholder="What did you think?"
                                        className="w-full p-2 rounded-lg border border-gray-600 bg-gray-900 text-white focus:border-highlight transition-all duration-300"
                                        rows="3"
                                    ></textarea>
                                </div>
                                <button
                                    type="submit"
                                    className="bg-primary hover:bg-primary-dark text-white py-2 px-6 rounded-lg font-semibold transition-all duration-300 transform hover:scale-105 glow"
                                >
                                    Submit
                                </button>
                            </form>
                        )}
                    </div>
                </div>

                {/* Divider */}
                <div className="divider my-8"></div>

                {/* Recent Comments Section */}
                <div className="bg-section-bright rounded-lg p-6 fade-in w-full">
                    <h3 className="text-xl font-semibold mb-4 text-highlight flex items-center gap-2">
                        <MessageSquare className="w-6 h-6" /> Recent Comments
                    </h3>
                    <div className="comment-section">
                        {comments.length > 0 ? (
                            comments.map((c, index) => (
                                <div key={c.ident} className="border-b border-gray-700 py-3 text-left">
                                    <p className="text-sm">
                                        <span className="font-semibold text-primary">{c.player.nickname}:</span>{" "}
                                        <span className="text-gray">{c.comment}</span>
                                    </p>
                                    <p className="text-xs text-gray-500">
                                        {new Date(c.commentedOn).toLocaleString()}
                                    </p>
                                </div>
                            ))
                        ) : (
                            <p className="text-gray-500 text-sm">No comments yet. Be the first!</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;