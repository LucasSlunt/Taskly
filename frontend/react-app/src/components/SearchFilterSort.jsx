
//takes in the searchQuery state and the setSearchQuery function as peramters 
function SearchFilterSort({ searchQuery, setSearchQuery }){
   
    const handleSearch = (e) => {
        const value = e.target.value; //get user input
        setSearchQuery(value); //update the state
    };

    return(
        <div className="search-form"> 
            <input
                    type="text"
                    placeholder="Search by task name.."
                    className="search-input"
                    value={searchQuery}
                    onChange={handleSearch}
                />
        </div>
    );
}

export default SearchFilterSort;